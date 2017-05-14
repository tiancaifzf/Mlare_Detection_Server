__author__ = 'Administrator'
#coding=utf-8
from androguard.core.bytecodes import apk, dvm
from androguard.core.analysis import analysis
import re
import sys
global count
count = 1

def get_permissions(path, filename):
    str = "Permission:"
    app = apk.APK(path)
    permission = app.get_permissions()
    file = permission
    print (permission)
    writeToTxt(str, file, filename)
    return permission

def get_apis(path, filename):
  app = apk.APK(path)
  app_dex = dvm.DalvikVMFormat(app.get_dex())
  app_x = analysis.newVMAnalysis(app_dex)
  methods = set()
  cs = [cc.get_name() for cc in app_dex.get_classes()]

  for method in app_dex.get_methods():
    g = app_x.get_method(method)
    if method.get_code() == None:
      continue

    for i in g.get_basic_blocks().get():
      for ins in i.get_instructions():
        output = ins.get_output()
        match = re.search(r'(L[^;]*;)->[^\(]*\([^\)]*\).*', output)
        if match and match.group(1) not in cs:
          methods.add(match.group())

  methods = list(methods)
  methods.sort()
  print ("methods:"+"\n")
  print (methods)
  str = "Methods:"
  file = methods
  writeToTxt(str, file, filename)
  return methods
def get_providers(path, filename):
    app = apk.APK(path)
    providers = app.get_providers()
    print ("providers:"+"\n")
    print (providers)
    str = "Providers:"
    file = providers
    writeToTxt(str, file, filename)
    return providers
def get_package(path, filename):
    app = apk.APK(path)
    packname = app.get_package()
    print ("packageName:"+"\n")
    print (packname)
    str = "PackageName:"
    file = packname
    writeToTxt(str, file, filename)
    return packname
def get_activities(path, filename):
    app = apk.APK(path)
    activitys = app.get_activities()
    print ("ActivityName:"+"\n")
    print (activitys)
    str = "Activitys:"
    file = activitys
    writeToTxt(str, file, filename)
    return activitys
def get_receivers(path, filename):
    app = apk.APK(path)
    receivers = app.get_receivers()
    print ("Receivers:"+"\n")
    print (receivers)
    str = "Receivers:"
    file = receivers
    writeToTxt(str, file, filename)
    return receivers
def get_services(path, filename):
    app = apk.APK(path)
    services = app.get_services()
    print ("Services:"+"\n")
    print (services)
    str = "Services:"
    file = services
    writeToTxt(str, file, filename)
    return services
def writeToTxt(str, file, filename):
    global count
    import os
    #fm = open('%d'%count+'.txt', 'w')
    #print(filename)
    #filepath = os.path.join("D:","Bad_Result")
    #filename = os.path.join(filepath, filename)
    # filename = "D:\Bad_Result"+filename
    #print(filename)
    #filename ="C:/Users/MAX-FZF/Desktop/Good_Result/" + filename
    filename = "/home/user/Server/Step1_txt/" + filename
    fm = open(filename,'w')
    #fm.write(str)
    #fm.write("\n")
    for i in file:
        tmp = i.split('.')
        final = tmp[-1]
        fm.write(final)
        fm.write("\t")
    fm.close()
    count += 1

def main(path, apkname):
  try:
      get_permissions(path, apkname)
  except:
      print("something wrong!")

if __name__ == '__main__':
    if len(sys.argv) == 2:
     path = "/home/user/Server/Up_Load_Apk/"+sys.argv[1]+".apk"
     #path = "C:/Users/MAX-FZF/Desktop/Good_after5063/" + sys.argv[1] + ".apk"
     filename = sys.argv[1]+".txt"
     main(path, filename)

