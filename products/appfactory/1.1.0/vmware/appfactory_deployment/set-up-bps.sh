 #!/bin/bash
function setup_bps {
unset OPTIND
while getopts w:r:e:v:h:o: option
do
        case "${option}"
        in
                w) working_dir=${OPTARG};;
                r) resorce_dir=${OPTARG};;
                e) environment=${OPTARG};;
                v) version=$OPTARG;;
                h) af_host_name=$OPTARG;;
                o) offset=$OPTARG;;
        esac
done

#AFHOME
APPFACTORY_HOME=$working_dir/appfactory/wso2appfactory-1.1.0/

 
#configure appfactory
pack_dir=$working_dir/bps/
mkdir -p $pack_dir
echo "Setting up BPS........"
/usr/bin/unzip  -q $resorce_dir/packs/wso2bps-${version}.zip   -d $pack_dir
BPS_HOME=$pack_dir/wso2bps-${version}
cp $resorce_dir/configs/bps-wso2server.sh $BPS_HOME/bin/wso2server.sh
mkdir $BPS_HOME/repository/conf/appfactory
cp $APPFACTORY_HOME/repository/conf/appfactory/appfactory.xml $BPS_HOME/repository/conf/appfactory
cat $resorce_dir/configs/appfactory.xml | sed -e "s@AF_HOST@$af_host_name@g" > $BPS_HOME/repository/conf/appfactory/appfactory.xml
#cp $resorce_dir/configs/appfactory-user-mgt.xml $BPS_HOME/repository/conf/user-mgt.xml
#cp $resorce_dir/configs/appfactory-registry.xml $BPS_HOME/repository/conf/registry.xml
cat $resorce_dir/configs/appfactory-carbon.xml | sed -e "s@AF_HOST@$af_host_name@g"  | sed -e "s@OFFSET@$offset@g" > $BPS_HOME/repository/conf/carbon.xml
cp $resorce_dir/configs/appfactory-humantask.xml $BPS_HOME/repository/conf/humantask.xml
cp -r $resorce_dir/configs/endpoints  $BPS_HOME/repository/conf/appfactory
cp $resorce_dir/configs/bps-wso2server.sh $BPS_HOME/bin

cp $resorce_dir/lib/mysql-connector-java-5.1.12-bin.jar $BPS_HOME/repository/components/lib
mkdir $BPS_HOME/repository/deployment/server/bpel/
mv $APPFACTORY_HOME/repository/deployment/server/bpel/* $BPS_HOME/repository/deployment/server/bpel/
cp $APPFACTORY_HOME/repository/components/plugins/org.wso2.carbon.appfactory.utilities_1.1.0.jar $BPS_HOME/repository/components/dropins/

}
